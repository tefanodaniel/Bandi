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
        <Card style={bandi_styles.submission_card} className="rounded shadow-sm border-0">
            <Card.Body>
                <Card.Title><b>{entry.name}</b></Card.Title>
                <Card.Text className="small font-italic" style={{color:"white"}}>Instruments: {entry.instruments.join(', ')}</Card.Text>
                <Card.Text className="small font-italic"> <a href={entry.avLink} style={{color:"white"}}>View Submission</a></Card.Text>
            </Card.Body>
        </Card>
    )
}


// class SotwSubmissionList extends React.Component {
//   constructor(props) {
//     super(props)
//
//
//   }
//
//   componentDidMount() {
//     let submission_ids = selectSotwEventSubmissionIds(this.props.store);
//     let event_id = selectChosenSotwEventId(this.props.store);
//     let submissions = selectSotwEventSubmissions(this.props.store);
//
//     // If an event is selected and there are submissions to show, fetch them
//     if(event_id !== -1 && submission_ids !== -1) {
//       console.log('fetching submissions')
//       this.props.getSubmissionsWrapper(submission_ids, event_id);
//     }
//   }
//
//   componentDidUpdate(prevProps) {
//     let event_id = selectChosenSotwEventId(this.props.store);
//     let prev_event_id = selectChosenSotwEventId(prevProps.store);
//
//     let submission_ids = selectSotwEventSubmissionIds(this.props.store);
//     let prev_submission_ids = selectSotwEventSubmissionIds(prevProps.store);
//
//     //let submissions = selectSotwEventSubmissions(this.props.store);
//
//     // If an event is selected and there are submissions to show, fetch them
//     if (prev_event_id !== event_id) {
//       if (event_id !== -1 && submission_ids !== -1) {
//         console.log('fetching submissions')
//         this.props.getSubmissionsWrapper(submission_ids, event_id);
//       }
//     }
//
//   }
//
//   render() {
//     let submissions = selectSotwEventSubmissions(this.props.store)
//     // let submission_ids = selectSotwEventSubmissionIds(this.props.store);
//     // let event_id = selectChosenSotwEventId(this.props.store);
//     // let submissions = selectSotwEventSubmissions(this.props.store);
//     //
//     // // If an event is selected and there are submissions to show, fetch them
//     // if(event_id !== -1 && submission_ids !== -1) {
//     //   this.props.getSubmissionsWrapper(submission_ids, event_id);
//     // }
//
//     if (submissions === -1) {
//       return (
//         <Container style={{marginTop:"250px", marginLeft:"600px"}} className="justify-content-md-center">
//           <h4> Stay Tuned ...</h4>
//         </Container>
//       )
//     } else {
//       const submissions_chunk = chunk(submissions,4);
//       const rows = submissions_chunk.map((sub_chunk, index) => {
//         const cols = sub_chunk.map((entry, index) => {
//           return (
//             <Col key={index} style={{height: "180px" , columnWidth: "250px"}}>
//               <SubmissionItem id={entry.submission_id} name={entry.musician_name} instruments={entry.instruments}  avLink={entry.avSubmission}/>
//             </Col>
//           );
//         });
//         return <Row key={index} style={{width: "1000px",marginTop:"50px", height: "250px"}} >{cols}</Row>
//       });
//
//       return (
//         <div style={{marginTop:"50px", marginLeft:"100px", height: "1000px"}}>
//           <Container fluid>{rows}</Container>
//         </div>
//       );
//     }
//   }
// }


const SotwSubmissionList = () => {
    const dispatch = useDispatch();
    let submission_ids = useSelector(selectSotwEventSubmissionIds);
    let event_id = useSelector(selectChosenSotwEventId);

    const [current_event_id, set_current_event_id] = useState(-1);

    useEffect(() => {
      if (event_id !== -1 && submission_ids !== -1 && event_id !== current_event_id) {
        console.log("in effect, doing api call with state", current_event_id)
        dispatch(getSubmissionsWrapper(submission_ids, event_id));
        set_current_event_id(event_id);
      }
    });



    let submissions = useSelector(selectSotwEventSubmissions)
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



// function mapStateToProps(state) {
//   return {
//     store: state
//   };
// } // end mapStateToProps
//
// function mapDispatchToProps(dispatch) {
//     return {
// 		    getSubmissionsWrapper: (submission_ids, event_id) => dispatch(getSubmissionsWrapper(submission_ids, event_id)),
//     }
// }

export default SotwSubmissionList;

//export default connect(mapStateToProps, mapDispatchToProps)(SotwSubmissionList);
