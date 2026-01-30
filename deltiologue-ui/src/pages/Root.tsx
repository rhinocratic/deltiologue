import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { PrimeReactProvider } from "primereact/api";

export default function Root() {
  return (
    <PrimeReactProvider>
      <div className="container mx-auto px-20">
        <Header />
        <Outlet />
        <Footer />
      </div>
    </PrimeReactProvider>
  );
}
