import { useNavigate } from "react-router-dom";
import styles from "../../styles/home/Header.module.css";

const Header = () => {
    const navigate = useNavigate();

    return (
        <div className={styles.headerContainer}>
            <div>
                <img src="../../src/assets/tlogo.svg" alt="logo" className={styles.logo} />
            </div>
            <ul className={styles.navLinks}>
                <li><a href="#" className={styles.link}>Product</a></li>
                <li><a href="#" className={styles.link}>Changelog</a></li>
                <li><a href="#" className={styles.link}>Pricing</a></li>
                <li><a href="#" className={styles.link}>Resources</a></li>
            </ul>
            <div>
                {/* Add onClick handlers to navigate */}
                <button className={styles.loginBtn} onClick={() => navigate("/signin")}>Login</button>
                <button className={styles.getStarted} onClick={() => navigate("/signup")}>Get Started</button>
            </div>
        </div>
    );
}

export default Header;
