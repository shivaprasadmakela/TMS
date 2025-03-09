import styles from "../../styles/home/Hero.module.css";
import { useNavigate } from "react-router-dom";


const Hero = () => {
const navigate = useNavigate();

    return (
        <div className={styles.heroContainer}>
            <img src="../../src/assets/heroheader.svg" alt="hero" className={styles.heroImage} />
            <div className={styles.heroTextContainer}>
                <h1 className={styles.heroText}>Collect all your product feedback <span className={styles.heroSpan}>in one location </span></h1>
                <p className={styles.heroSubText}>
                    Nexus is the most delightful way to capture
                    feedback, extract insights, create roadmaps, and
                    communicate your releases.</p>
                <div>
                    <button className={styles.getStarted} onClick={()=>navigate("/signup")}>Get started</button>
                    <button className={styles.bookADemo}>Book a demo</button>
                </div>

            </div>
        </div>
    );
}

export default Hero;