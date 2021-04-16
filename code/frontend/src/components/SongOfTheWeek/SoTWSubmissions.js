import React from 'react';
import {useSelector, shallowEqual} from "react-redux";
import {Container, Row, Col, Card} from "react-bootstrap";
import card_bg from "../../images/card.jpg";
import {bandi_styles} from "../../styles/bandi_styles";

const selectEventSubmissions = (state) => {
    if(!state.sotw_event_reducer.chosen_event_submissions)
        return -1;
    else
        return state.sotw_event_reducer.chosen_event_submissions.map(item => item)
}


const chunk = (arr, chunkSize = 1, cache = []) => {
    const tmp = [...arr]
    if (chunkSize <= 0) return cache
    while (tmp.length) cache.push(tmp.splice(0, chunkSize))
    return cache
}


const styles = {
    card: {
        borderStyle: "dashed",
        height: "180px",
        width: "200px",
        backgroundImage: `url(${card_bg})`,
        backgroundPosition: "center",
        backgroundSize:"cover",
        color: "white"
    }
}


const SubmissionItem = ( entry ) => {
    return (
        <Card style={bandi_styles.submission_card} className="rounded shadow-sm border-0">
            <Card.Body>
                <Card.Title><b>{entry.name}</b></Card.Title>
                <Card.Text className="small font-italic" style={{color:"white"}}>Instruments: {entry.instruments.join(', ')}</Card.Text>
                <Card.Text className="small font-italic"> <a href={entry.avLink} style={{color:"white"}}>View Submission</a></Card.Text>
            </Card.Body>
        </Card>
    )
}


const SoTWSubmissions = () => {
    let submissions = useSelector(selectEventSubmissions, shallowEqual)

    if(submissions === -1)
    {
        return (
            <Container style={{marginTop:"250px", marginLeft:"600px"}} className="justify-content-md-center">
                <h4> Stay Tuned ...</h4>
            </Container>
        )
    }
    else {
        const submissions_chunk = chunk(submissions,4);

        const rows = submissions_chunk.map((sub_chunk, index) => {
            const cols = sub_chunk.map((entry, index) => {
                return (
                    <Col key={index} style={{height: "180px" , columnWidth: "250px"}}>
                        <SubmissionItem id={entry.submission_id} name={entry.musician_name} instruments={entry.instruments}  avLink={entry.avSubmission}/>
                    </Col>
                );
            });
            return <Row key={index} style={{width: "1000px",marginTop:"50px", height: "250px"}} >{cols}</Row>
            });
        return (
            <div style={{marginTop:"50px", marginLeft:"100px", height: "1000px"}}>
            <Container fluid>
                {rows}
            </Container>
            </div>
        )
    }
}

export default SoTWSubmissions
