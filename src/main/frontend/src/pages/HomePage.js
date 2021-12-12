import { Helmet } from "react-helmet";
import React, { useEffect, useState } from 'react';
import axios from "axios";
import Modal from 'react-awesome-modal';
import update_icon from "./update_icon.png";
import delete_icon from "./delete_icon.png";

export function HomePage(props) {

    const [logs, setLogs] = useState([]);
    const [createVisible, setCreateVisible] = useState(false);
    const [updateVisible, setUpdateVisible] = useState(false);
    const [deleteVisible, setDeleteVisible] = useState(false);
    const [deleteAccountVisible, setDeleteAccountVisible] = useState(false);
    const [username, setUsername] = useState("");
    const [deleteId, setDeleteId] = useState("");
    const [newId, setNewId] = useState("");
    const [newUser, setNewUser] = useState("");
    const [newBrewDate, setNewBrewDate] = useState("");
    const [newType, setNewType] = useState("");
    const [newBatchSize, setNewBatchSize] = useState("");
    const [newDescription, setNewDescription] = useState("");
    const [logsEmpty, setLogsEmpty] = useState(true);


    useEffect(() => {

        axios({
            'method': 'GET',
            'url': `${process.env.hostUrl || 'http://localhost:8080'}/user/info`,
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((res) => {
            setUsername(res.data.username);
            axios({
                'method': 'POST',
                'url': `${process.env.hostUrl || 'http://localhost:8080'}/api/get_logs`,
                'data': { user: res.data.username }
            }).then((response) => {
                let tempList = [];
                for (let i = 0; i < response.data.length; i++) {
                    tempList.push(response.data[i])
                }
                console.log(tempList);
                if(tempList.length>0){
                    setLogsEmpty(false);
                }
                setLogs(tempList.map((element) => {
                    return (
                        <tr id={element.id}><td>{element.brewDate}</td><td>{element.type}</td><td>{element.batchSize}</td><td>{element.description}</td>
                            <td><img src={update_icon} width="30px" id={element.id} onClick={e => handleUpdateLog(element.id, element.username, element.brewDate, element.batchSize, element.type, element.description)} /></td>
                            <td><img src={delete_icon} width="30px" id={element.id} onClick={showDeletePopup} /></td>
                        </tr>)
                }));
            })
        })
    }, []);

    function showCreatePopup(event) {
        setCreateVisible(true);
    }

    function hideCreatePopup(event) {
        setCreateVisible(false);
    }

    function showDeletePopup(event) {
        setDeleteId(event.target.id)
        setDeleteVisible(true);
    }

    function hideDeletePopup(event) {
        setDeleteVisible(false);
    }

    function handleUpdateLog(id, user, brewDate, batchSize, type, description) {
        setNewId(id);
        setNewUser(user)
        setNewBrewDate(brewDate);
        setNewBatchSize(batchSize);
        setNewType(type);
        setNewDescription(description);
        setUpdateVisible(true)
    }

    function hideUpdatePopup(event) {
        setUpdateVisible(false);
    }

    function handleSubmitCreate(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080/api/create'}`,
            'data': {
                id: null,
                user: null,
                brewDate: newBrewDate,
                batchSize: parseInt(newBatchSize),
                type: newType,
                description: newDescription
            },
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            console.log(response.data)
        })

        setCreateVisible(false);
        window.location.reload(true);
        event.preventDefault();
    }

    function handleSubmitUpdate(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080/api/update'}`,
            'data': {
                id: newId,
                username: newUser,
                brewDate: newBrewDate,
                batchSize: parseInt(newBatchSize),
                type: newType,
                description: newDescription
            },
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            console.log(response.data)
        })

        setUpdateVisible(false);
        window.location.reload(true);
        event.preventDefault();
    }

    function handleDeleteLog(event) {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080/api/delete'}`,
            'data': deleteId
            ,
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            console.log(response.data)
        })

        window.location.reload(true);
    }

    function showDeleteAccountPopup() {
        setDeleteAccountVisible(true);
    }

    function hideDeleteAccountPopup() {
        setDeleteAccountVisible(false);
    }

    function handleDeleteAccount(event) {
        axios({
            'method': 'DELETE',
            'url': `${process.env.hostUrl || 'http://localhost:8080/user'}`,
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            alert("Account successfully deleted")
            props.history.push("/login")
        })
    }

    function handleLogout() {
        axios({
            'method': 'POST',
            'url': `${process.env.hostUrl || 'http://localhost:8080/user/logout'}`,
            headers: {
                "Content-Type": "application/json"
            },
            withCredentials: true
        }).then((response) => {
            props.history.push("/login")
        })
    }

    return (<div className="homepage">
        <Helmet>
            <meta charSet="UTF-8" />
            <title>Homepage</title>
        </Helmet>

        <div class="navbar">
            <div class="dropdown">
                <button class="dropbtn">Manage account
                    <i class="fa fa-caret-down"></i>
                </button>
                <div class="dropdown-content">
                    <a id="deleteButton" onClick={showDeleteAccountPopup}>Delete account</a>
                    <a onClick={handleLogout} >Logout</a>
                </div>
            </div>
        </div>
{!logsEmpty?
        <div id="log-div">
            <table id="logs-table">
                <tr>
                    <th><b>Brew date</b></th>
                    <th><b>Type</b></th>
                    <th><b>Batch size</b></th>
                    <th><b>Description</b></th>
                    <th></th>
                    <th></th>
                </tr>
                {logs}
            </table>
        </div>:""
        }

        <Modal id="createModal" visible={createVisible} width="500" height="400" effect="fadeInUp"
            onClickAway={hideCreatePopup}>
            <form id="createPopup" onSubmit={handleSubmitCreate} >
                <label><b>Brew date</b></label>
                <input name="brewDate" type="text" placeholder="brew date" onChange={e => setNewBrewDate(e.target.value)} ></input>
                <label><b>Type</b></label>
                <input name="type" type="text" placeholder="type" onChange={e => setNewType(e.target.value)} ></input>
                <label><b>Batch size</b></label>
                <input name="batchSize" type="number" placeholder="batch size" onChange={e => setNewBatchSize(parseInt(e.target.value))} ></input>
                <label><b>Description</b></label>
                <input name="description" type="textarea" placeholder="description" onChange={e => setNewDescription(e.target.value)} ></input>

                <div className="buttons">
                    <button id="submitCreate" type="submit">Log new brew</button>
                    <button id="cancelCreate" type="reset" onClick={hideCreatePopup}>Cancel</button>
                </div>

            </form>
        </Modal>



        <Modal id="updateModal" visible={updateVisible} width="500" height="400" effect="fadeInUp"
            onClickAway={hideUpdatePopup}>
            <form id="updatePopup" onSubmit={handleSubmitUpdate} >
                <label><b>Brew date</b></label>
                <input name="brewDate" type="text" value={newBrewDate} onChange={e => setNewBrewDate(e.target.value)} ></input>
                <label><b>Type</b></label>
                <input name="type" type="text" value={newType} onChange={e => setNewType(e.target.value)} ></input>
                <label><b>Batch size</b></label>
                <input name="batchSize" type="number" value={newBatchSize} onChange={e => setNewBatchSize(e.target.value)} ></input>
                <label><b>Description</b></label>
                <input name="description" type="textarea" value={newDescription} onChange={e => setNewDescription(e.target.value)} ></input>

                <div className="buttons">
                    <button id="submitUpdate" type="submit">Update log</button>
                    <button id="cancelUpdate" type="reset" onClick={hideUpdatePopup}>Cancel</button>
                </div>

            </form>
        </Modal>

        <Modal visible={deleteVisible} width="300" height="150" effect="fadeInUp">
            <div id="deleteLogModal">
                <div>Are you sure?</div>
                <div>This action cannot be undone</div>
                <div className="buttons">
                    <button id="confirmDelete" onClick={handleDeleteLog}>Yes, delete</button>
                    <button id="cancelDelete" onClick={hideDeletePopup}>No</button>
                </div>
            </div>
        </Modal>

        <Modal visible={deleteAccountVisible} width="300" height="150" effect="fadeInUp">
            <div id="deleteLogModal">
                <div>Are you sure?</div>
                <div>This action cannot be undone</div>
                <div className="buttons">
                    <button id="confirmDelete" onClick={handleDeleteAccount}>Yes, delete</button>
                    <button id="cancelDelete" onClick={hideDeleteAccountPopup}>No</button>
                </div>
            </div>
        </Modal >
        <div id="addNewButton">
            <button onClick={showCreatePopup} >{logsEmpty? "Add your first brew here":"Add new brew log"}</button>
        </div>


    </div >)
}