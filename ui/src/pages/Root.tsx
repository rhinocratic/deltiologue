import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";

export default function Root() {
  return (
    <div className="container mx-auto px-20">
      <Header />
      <Outlet />
      <Footer />
    </div>
  );
}
