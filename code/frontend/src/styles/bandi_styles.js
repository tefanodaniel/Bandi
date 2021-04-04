import band_img from "../images/band_jumbo.jpg";
import music_img from "../images/music_jumbo.jpg";
import sdate_img from "../images/speeddate_jumbo.jpg";
import sotw_img from "../images/sotw_jumbo.jpg";
import discover_bg from "../images/discover_bg.jpg";
import card_bg from "../images/card.jpg";

export const bandi_styles = {
    discover_background : {
        backgroundImage:`url(${discover_bg})`,
        height: '1500px', //handcrafting it for now.
        backgroundPosition: "center",
        backgroundSize: "cover",
        backgroundRepeat: "repeat-y"
    },
    jumbo_music: {
        backgroundAttachment: "static",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${music_img})`
    },
    jumbo_band: {
        backgroundAttachment: "static",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${band_img})`
    },
    jumbo_sotw: {
        backgroundAttachment: "static",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${sotw_img})`
    },
    jumbo_sdate: {
        backgroundAttachment: "static",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundSize: "cover",
        backgroundImage: `linear-gradient(to bottom, rgba(0,0,0,0.6) 0%,rgba(0,0,0,0.6) 100%), url(${sdate_img})`
    },
    discover_row_col: {
        width: "200px",
        height:"100px"
    },
    musician_card: {
        borderStyle: "dashed",
        height: "180px",
        width: "200px",
        backgroundImage: `url(${card_bg})`,
        backgroundPosition: "center",
        backgroundSize:"cover",
        color: "white"
    },
    band_card: {
        borderStyle: "dashed",
        height: "180px",
        width: "200px",
        backgroundImage: `url(${card_bg})`,
        backgroundPosition: "center",
        backgroundSize:"cover",
        color: "white",
        marginTop: "20px",
        marginLeft: "20px"
    },
    sotw_desc: {
            borderStyle: "none",
            height: "180px",
            marginTop: "20px",
            marginLeft: "100px",
            marginBottom:"80px"
    },
    submission_card: {
        borderStyle: "dashed",
        height: "180px",
        width: "200px",
        backgroundImage: `url(${card_bg})`,
        backgroundPosition: "center",
        backgroundSize:"cover",
        color: "white"
    }
}
