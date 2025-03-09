import AllOther from "../components/home/AllOther";
import Header  from "../components/home/Header";
import Hero from "../components/home/Hero";

const Home = () => {

  return (
    <div className="container">
        <Header />
        <Hero />
        <AllOther />
    </div>
  );
};

export default Home;
