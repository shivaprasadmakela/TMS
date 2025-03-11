import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { login, register } from "../store/slices/authSlice";
import styles from "../../src/styles/signin&singup/AuthForm.module.css";
import welcomeImage from "../assets/welcome-to-monday.avif";
import { RootState } from "../store";

interface AuthFormProps {
  title: string;
  subTitle?: string;
  buttonText: string;
  showNameField?: boolean;
  bottomText: string;
}

const AuthForm: React.FC<AuthFormProps> = ({ title, subTitle, buttonText, bottomText, showNameField }) => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { loading, error } = useSelector((state: RootState) => state.auth);

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (buttonText.toLowerCase() === "signin") {
      dispatch(login({ email: formData.email, password: formData.password }) as any)
        .unwrap()
        .then(() => navigate("/dashboard"))
        .catch(() => {});
    } else {
      dispatch(register(formData) as any)
        .unwrap()
        .then(() => navigate("/signin"))
        .catch(() => {});
    }
  };

  return (
    <div className={styles.authContainer}>
      <div className={styles.authFormContainer}>
        <h2 className={styles.title}>{title}</h2>
        {subTitle && <p className={styles.subTitle}>{subTitle}</p>}
        
        {error && <p className={styles.errorText}>{error}</p>}

        <form onSubmit={handleSubmit} className={styles.authForm}>
          {showNameField && (
            <>
              <input
                className={styles.formInput}
                type="text"
                name="firstName"
                placeholder="First Name"
                value={formData.firstName}
                onChange={handleChange}
                required
              />
              <input
                className={styles.formInput}
                type="text"
                name="lastName"
                placeholder="Last Name"
                value={formData.lastName}
                onChange={handleChange}
                required
              />
            </>
          )}
          <input
            type="email"
            className={styles.formInput}
            name="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />
          <input
            type="password"
            name="password"
            className={styles.formInput}
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />
          <button type="submit" className={styles.formButton} disabled={loading}>
            {loading ? "Processing..." : buttonText}
          </button>
        </form>

        <p>
          By proceeding, you agree to the{" "} <br />
          <a href="#" className={styles.termsLink}>Terms of Service</a> and{" "}
          <a href="#" className={styles.termsLink}>Privacy Policy</a>.
        </p>

        <h5>
          {bottomText}{" "}
          <a href={buttonText.toLowerCase() === "signin" ? "/signup" : "/signin"}>
            {buttonText.toLowerCase() === "signin" ? "Sign Up" : "Sign In"}
          </a>
        </h5>
      </div>
      
      <img src={welcomeImage} alt="Welcome" className={styles.mainImage} />
    </div>
  );
};

export default AuthForm;
