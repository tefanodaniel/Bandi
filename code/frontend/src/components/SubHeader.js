import React from 'react';
import Navbar from 'react-bootstrap/Navbar';

const SubHeader = ( input ) => {
    return (
        <Navbar expand="lg" variant="light" bg="light" className="mx-auto bg-transparent">
            <Navbar.Brand className="mx-auto">
                {input.text}
            </Navbar.Brand>
        </Navbar>
    )
}

export default SubHeader;
