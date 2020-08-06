import React, { useRef, useState, useEffect } from 'react'
import axios from 'axios';
import Modal from 'react-bootstrap/Modal'
import './Dropzone.css';
import { Button, Form, FormGroup, Label, Input} from 'reactstrap'
import $ from "jquery";
import {} from "jquery.cookie";


const btnStyle = {
    color: "white",
    background: "teal",
    padding: ".375rem .75rem",
    border: "1px solid teal",
    borderRadius: ".25rem",
    fontSize: "1rem",
    lineHeight: 1.5,
  }
  

  const CreateProject = (props) => {
    //props로 userid 받음.
    const [commitID, setCommitID] = useState('');
    const [description, setDescription] = useState('');
    const [projectID, setProjectID] = useState('');
    const [category, setCategory] = useState('');
    const [show, setShow] = useState(false);
    const fileInputRef = useRef();
    const modalImageRef = useRef();
    const modalRef = useRef();
    const progressRef = useRef();
    const uploadRef = useRef();
    const uploadModalRef = useRef();
    const [selectedFiles, setSelectedFiles] = useState([]);
    const [validFiles, setValidFiles] = useState([]);
    const [unsupportedFiles, setUnsupportedFiles] = useState([]);
    const [errorMessage, setErrorMessage] = useState('');

    const handleModal = () => {
        setCommitID('');
        setDescription('');
        setProjectID('');
        setShow(!show);
        setCategory('');
        setSelectedFiles([]);
        setValidFiles([]);
        setUnsupportedFiles([]);
        setErrorMessage('');
    };
    

    useEffect(() => {
        let filteredArr = selectedFiles.reduce((acc, current) => {
            const x = acc.find(item => item.name === current.name);
            if (!x) {
                return acc.concat([current]);
            } else {
                return acc;
            }
        }, []);
        setValidFiles([...filteredArr]);
        
    }, [selectedFiles]);

    const preventDefault = (e) => {
        e.preventDefault();
        // e.stopPropagation();
    }

    const dragOver = (e) => {
        preventDefault(e);
    }

    const dragEnter = (e) => {
        preventDefault(e);
    }

    const dragLeave = (e) => {
        preventDefault(e);
    }

    const fileDrop = (e) => {
        preventDefault(e);
        const files = e.dataTransfer.files;
        if (files.length) {
            handleFiles(files);
        }
    }

    const filesSelected = () => {
        if (fileInputRef.current.files.length) {
            handleFiles(fileInputRef.current.files);
        }
    }

    const fileInputClicked = () => {
        fileInputRef.current.click();
    }

    const handleFiles = (files) => {
        for(let i = 0; i < files.length; i++) {
            if (validateFile(files[i])) {
                setSelectedFiles(prevArray => [...prevArray, files[i]]);
            } else {
                files[i]['invalid'] = true;
                setSelectedFiles(prevArray => [...prevArray, files[i]]);
                setErrorMessage('File type not permitted');
                setUnsupportedFiles(prevArray => [...prevArray, files[i]]);
            }
        }
    }

    const validateFile = (file) => {
        const validTypes = ['audio/mpeg','audio/wav'];
        if (validTypes.indexOf(file.type) === -1) {
            return false;
        }

        return true;
    }

    const fileSize = (size) => {
        if (size === 0) {
            return '0 Bytes';
        }
        const k = 1024;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(size) / Math.log(k));
        return parseFloat((size / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    }

    const fileType = (fileName) => {
        return fileName.substring(fileName.lastIndexOf('.') + 1, fileName.length) || fileName;
    }

    const removeFile = (name) => {
        const index = validFiles.findIndex(e => e.name === name);
        const index2 = selectedFiles.findIndex(e => e.name === name);
        const index3 = unsupportedFiles.findIndex(e => e.name === name);
        validFiles.splice(index, 1);
        selectedFiles.splice(index2, 1);
        setValidFiles([...validFiles]);
        setSelectedFiles([...selectedFiles]);
        if (index3 !== -1) {
            unsupportedFiles.splice(index3, 1);
            setUnsupportedFiles([...unsupportedFiles]);
        }
    }


    const closeModal = () => {
        modalRef.current.style.display = "none";
        modalImageRef.current.style.backgroundImage = 'none';
    }

    const uploadFiles = async () => {
        console.log('hhhhhhhhhhhhhhhhhh')
        uploadModalRef.current.style.display = 'block';
        uploadRef.current.innerHTML = 'File(s) Uploading...';
        
        for (let i = 0; i < validFiles.length; i++) {
            const formData = new FormData();
            formData.append('key', '');
            formData.append('userid', props.userid);
            formData.append('projectID', projectID);
            formData.append('description', description);
            formData.append('commitID', commitID);
            formData.append('category', category);
            formData.append('files', validFiles[i]);
            
            
            axios.post('http://localhost:3001/api/create/project', formData, {
                headers:{'content-type': 'multipart/form-data'},
                onUploadProgress: (progressEvent) => {
                    const uploadPercentage = Math.floor((progressEvent.loaded / progressEvent.total) * 100);
                    progressRef.current.innerHTML = `${uploadPercentage}%`;
                    progressRef.current.style.width = `${uploadPercentage}%`;

                    if (uploadPercentage === 100) {
                        uploadRef.current.innerHTML = 'File(s) Uploaded';
                        validFiles.length = 0;
                        setValidFiles([...validFiles]);
                        setSelectedFiles([...validFiles]);
                        setUnsupportedFiles([...validFiles]);
                    }
                }
            })
            .catch(() => {
                uploadRef.current.innerHTML = `<span class="err-or">Error Uploading File(s)</span>`;
                progressRef.current.style.backgroundColor = 'red';
            })
        }
    }

    const closeUploadModal = () => {
        uploadModalRef.current.style.display = 'none';
    }

    return (
        <div>
            <button style={btnStyle} onClick={() => handleModal()}>Create new project</button>
            <Modal show={show}>
                <Modal.Header>
                    <b>Create new project</b>
                    {/* 닫기 버튼 */}
                    <div align="right" style={{padding:"10px"}}>
                    <button type="button" className="close" aria-label="Close" onClick={()=>handleModal()}>
                    <span aria-hidden="true">&times;</span>
                    </button>
                    </div>
                </Modal.Header>
                <Modal.Body>
                <Form className="login-form">
                    <FormGroup>
                        <Label>Project name</Label>
                        <Input id='projectID' type="text" value={projectID} onChange={e=> setProjectID(e.target.value)}/>
                    </FormGroup>
                    <FormGroup>
                        <Label>Description</Label>
                        <Input id='description' type="text" value={description} onChange={e=> setDescription(e.target.value)}/>
                    </FormGroup>
                </Form>
                </Modal.Body>
                {/* First commit */}
                <Form className="login-form"><b>First commit</b></Form>
                <Modal.Footer>

                <Form className="login-form">
                    <FormGroup>
                        <Label>CommitID</Label>
                        <Input id='commitID' type="text" placeholder="Commit ID" value={commitID} onChange={e=> setCommitID(e.target.value)}/>
                    </FormGroup>
                    <FormGroup>
                        <Label>Category</Label>
                        <Input id='category' type="text" placeholder="category" value={category} onChange={e=> setCategory(e.target.value)}/>
                    </FormGroup>
                    {/* dropbox 시작 */}
                    <FormGroup>
                    <div>
                        {unsupportedFiles.length ? <p>Please remove all unsupported files.</p> : ''}
                        <div className="drop-container"
                            onDragOver={dragOver}
                            onDragEnter={dragEnter}
                            onDragLeave={dragLeave}
                            onDrop={fileDrop}
                            onClick={fileInputClicked}
                        >
                            {
                                validFiles.length > 0
                            ?
                            <div>
                            {
                                validFiles.map((data, i) => 
                                    <div className="file-status-bar" key={i}>
                                        <div onClick={!data.invalid ? null : () => removeFile(data.name)}>
                                            <div className="file-type-logo"></div>
                                            <div className="file-type">{fileType(data.name)}</div>
                                            <span className={`file-name ${data.invalid ? 'file-error' : ''}`}>{data.name}</span>
                                            <span className="file-size">({fileSize(data.size)})</span> {data.invalid && <span className='file-error-message'>({errorMessage})</span>}
                                            <span className="file-remove" onClick={() => removeFile(data.name)}>X</span>
                                        </div>
                                    </div>
                                )
                            }
                            </div>
                            :
                            <div className="drop-message">
                                <div className="upload-icon"></div>
                                Drag & Drop files here or click to select file(s)
                            </div>
                            }
                            <input
                                ref={fileInputRef}
                                className="file-input"
                                type="file"
                                multiple
                                onChange={filesSelected}
                            />
                        </div>
                        {unsupportedFiles.length === 0 && validFiles.length
                        ?
                        <button className="file-upload-btn" onClick={() => uploadFiles()}>Upload Files</button>
                        : ''} 
                    </div>
                    <div className="modal" ref={modalRef}>
                        <div className="overlay"></div>
                        <span className="close" onClick={(() => closeModal())}>X</span>
                        <div className="modal-image" ref={modalImageRef}></div>
                    </div>

                    <div className="upload-modal" ref={uploadModalRef}>
                        <div className="overlay"></div>
                        <div className="close" onClick={(() => closeUploadModal())}>X</div>
                        <div className="progress-container">
                            <span ref={uploadRef}></span>
                            <div className="progress">
                                <div className="progress-bar" ref={progressRef}></div>
                            </div>
                        </div>
                    </div>
                    </FormGroup>
                    
                    {/* dropbox 끝 */}
                </Form>
                </Modal.Footer>
            </Modal>
        </div>
    )
            

}

export default CreateProject