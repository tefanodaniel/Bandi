import React from 'react';
import { render } from "react-dom";
//import { Link } from 'react-router-dom';
import { withRouter } from "react-router-dom";
import {getBackendURL} from "../utils/api";
import SimpleReactFooter from "simple-react-footer";

class Footer extends React.Component {
    render() {
        const description = "Bandi is a social media app that allows musicians to connect with other musicians and bands in their area. Users are able to participate in speed matching events as well as Song Of The Week events, as well as connecting as friends with other users.";
        const title = "Bandi";
        const columns = [
         {
            title: "Resources",
            resources: [
                {
                name: "Contact Us",
                link: "/ContactUs"
                },
                {
                name: "About",
                link: "/about"
                }
            ]
         }
    ];
    return <SimpleReactFooter
        description={description}
        title={title}
        columns={columns}
        linkedin="https://www.linkedin.com/in/noah-johnson-799314153/"
        facebook="https://www.facebook.com/justsurprisememe/photos/a.186902585196734/856981594855493/"
        twitter="https://twitter.com/dril"
        instagram="https://www.instagram.com/world_record_egg/"
        youtube="https://www.youtube.com/watch?v=dQw4w9WgXcQ"
        pinterest="https://www.pinterest.com/njohnson17/third-street-texts-because-the-website-is-stupid/"
        copyright="black"
        iconColor="black"
        backgroundColor="white"
        fontColor="black"
        copyrightColor="black"
    />;
    };
}


/*const Footer = () => {
    <footer className="footer">
        <li><Link to="/ContactUs">Contact us:</Link></li>
    </footer>
}*/
export default withRouter(Footer);