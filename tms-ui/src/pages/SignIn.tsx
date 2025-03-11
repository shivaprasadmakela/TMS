import AuthForm from "../components/AuthForm";

const SignIn = () => {

  const title ="Log in to your account";
  const bottomText = "Don't have an account yet?";

  return <AuthForm title={title} bottomText={bottomText} buttonText="SignIn" />;
};

export default SignIn;
