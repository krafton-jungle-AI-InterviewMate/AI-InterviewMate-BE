// <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>

const fileInput = document.getElementById("fileUpload");

let fileName;
let fileSize;
let file;

fileInput.onchange = async () => {
    file = fileInput.files[0];
    console.log(file);

    fileName = file.name;
    fileSize = file.size;


    const url = `http://localhost:8080`;
    let uploadId;
    let newFilename;
    try {
        let start = new Date();
        // 1. Spring Boot 서버로 멀티파트 업로드 시작 요청합니다.
        axios({
            url: url + "/initiate-upload",
            method: "POST",
            data: {
                "fileName": fileName,
            }
        }).then((res) => {
            uploadId = res.data.uploadId;
            newFilename = res.data.fileName; // 서버에서 생성한 새로운 파일명
            console.log(res);
        });

        // let res = await axios.post(`${url}/initiate-upload`, {fileName: fileName});
        // const uploadId = res.data.uploadId;
        // const newFilename = res.data.fileName; // 서버에서 생성한 새로운 파일명
        // console.log(res);

        // 세션 스토리지에 업로드 아이디와 파일 이름을 저장합니다.
        sessionStorage.setItem('uploadId', uploadId);
        sessionStorage.setItem('fileName', newFilename);

        // 청크 사이즈와 파일 크기를 통해 청크 개수를 설정합니다.
        const chunkSize = 10 * 1024 * 1024; // 10MB
        const chunkCount = Math.floor(fileSize / chunkSize) + 1;
        console.log(`chunkCount: ${chunkCount}`);

        let multiUploadArray = [];

        for (let uploadCount = 1; uploadCount < chunkCount + 1; uploadCount++) {
            // 청크 크기에 맞게 파일을 자릅니다.
            let start = (uploadCount - 1) * chunkSize;
            let end = uploadCount * chunkSize;
            let fileBlob = uploadCount < chunkCount ? file.slice(start, end) : file.slice(start);

            // 3. Spring Boot 서버로 Part 업로드를 위한 미리 서명된 URL 발급 바듭니다.
            let preSignedUrl;
            axios({
                url: url + "/upload-signed-url",
                method: "POST",
                data: {
                    "fileName": newFilename,
                    "partNumber": uploadCount,
                    "uploadId": uploadId,
                }
            }).then((res) => {
                preSignedUrl = res.data.preSignedUrl;
                console.log(`preSignedUrl ${uploadCount} : ${preSignedUrl}`);
                console.log(fileBlob);
            });

            // let getSignedUrlRes = await axios.post(`${url}/upload-signed-url`, {
            //     fileName: newFilename,
            //     partNumber: uploadCount,
            //     uploadId: uploadId
            // });

            // let preSignedUrl = getSignedUrlRes.data.preSignedUrl;
            // console.log(`preSignedUrl ${uploadCount} : ${preSignedUrl}`);
            // console.log(fileBlob);

            // 3번에서 받은 미리 서명된 URL과 PUT을 사용해 AWS 서버에 청크를 업로드합니다,
            let uploadChunk = await fetch(preSignedUrl, {
                method: 'PUT',
                body: fileBlob
            });
            console.log(uploadChunk);
            // 응답 헤더에 있는 Etag와 파트 번호를 가지고 있습니다.
            let EtagHeader = uploadChunk.headers.get('ETag').replaceAll('\"', '');
            console.log(EtagHeader);
            let uploadPartDetails = {
                awsETag: EtagHeader,
                partNumber: uploadCount
            };

            multiUploadArray.push(uploadPartDetails);
        }

        console.log(multiUploadArray);
        // 6. 모든 청크 업로드가 완료되면 Spring Boot 서버로 업로드 완료 요청을 보냅니다.
        // 업로드 아이디 뿐만 아니라 이 때 Part 번호와 이에 해당하는 Etag를 가진 'parts'를 같이 보냅니다.

        axios({
            url: url + "/complete-upload",
            method: "POST",
            data: {
                "fileName": newFilename,
                "parts": multiUploadArray,
                "uploadId": uploadId,
            }
        }).then((res) => {
            console.log(res.data, ' 업로드 완료 응답값');
            let end = new Date();
            console.log("파일 업로드 하는데 걸린 시간 : " + (end - start) + "ms")
        });

        // const completeUpload = await axios.post(`${url}/complete-upload`, {
        //     fileName: newFilename,
        //     parts: multiUploadArray,
        //     uploadId: uploadId
        // });
        // let end = new Date();
        // console.log("파일 업로드 하는데 걸린 시간 : " + (end - start) + "ms")
        // console.log(completeUpload.data, ' 업로드 완료 응답값');
    } catch (err) {
        console.log(err, err.stack);
    }
};