import AuthForm from "../components/AuthForm";

const SignUp = () => {

  const title = "Welcome to TMS";
  const subTitle = "Get started - it's free.";
  const bottomText = "Already have an account?";
  return <AuthForm title={title} subTitle={subTitle} bottomText={bottomText} buttonText="SignUp" showNameField />;
};

export default SignUp;
