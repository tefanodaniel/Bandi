import React, { useState, useEffect } from 'react';
import {useSelector, useDispatch, shallowEqual} from "react-redux";
import {Container, Row, Col, Card} from "react-bootstrap";
import {bandi_styles} from "../../styles/bandi_styles";
import {chunk} from "../../utils/miscellaneous";
import {selectChosenSotwEventId, selectSotwEventSubmissions, selectSotwEventSubmissionIds} from "../../selectors/sotw_selector";
import {getSubmissionsWrapper} from "../../actions/sotw_event_actions";

import { connect } from 'react-redux';

const SubmissionItem = ( entry ) => {
    return (
        <Card className="bandi-card submission-item">
            <Card.Body>
                <Card.Title><b>{entry.name}</b></Card.Title>
                <Card.Text className="small font-italic">Instruments: {entry.instruments.join(', ')}</Card.Text>
                <Card.Text className="small font-italic"> <a href={entry.avLink}>View Submission</a></Card.Text>
            </Card.Body>
        </Card>
    )
}

const SotwSubmissionList = () => {
    const dispatch = useDispatch();
    let submission_ids = useSelector(selectSotwEventSubmissionIds);
    let event_id = useSelector(selectChosenSotwEventId);

    const [current_event_id, set_current_event_id] = useState(-1);

    useEffect(() => {
      if (event_id !== -1 && submission_ids !== -1 && event_id !== current_event_id) {
        dispatch(getSubmissionsWrapper(submission_ids, event_id));
        set_current_event_id(event_id);
      }
    });



    let submissions = useSelector(selectSotwEventSubmissions)
    if(submissions === -1)
    {
        return (
            <div class="sotw-submission-list-placeholder">
            <p id="sotw-submission-list-placeholder-text">
                Here's where you'll see other Bandi members' submissions for the currently
                selected Song of the Week!
            </p>
        </div>
        )
    }
    else {
        const submissions_chunk = chunk(submissions,4);

        const rows = submissions_chunk.map((sub_chunk, index) => {
            const cols = sub_chunk.map((entry, index) => {
                return (
                    <Col key={index}>
                        <SubmissionItem id={entry.submission_id} name={entry.musician_name} instruments={entry.instruments}  avLink={entry.avSubmission}/>
                    </Col>
                );
            });
            return <Row key={index}>{cols}</Row>
            });
        return (
            <Container className="sotw-submission-list-items" fluid>
                {rows}
            </Container>
        )
    }
}

export default SotwSubmissionList;
