import { Outlet } from "react-router-dom";
import Header from "../components/Header";
import Footer from "../components/Footer";
import { PrimeReactProvider } from "primereact/api";
import Auth0ProviderWithRedirectCallback from "../components/auth/Auth0ProviderWithRedirectCallback";

export default function Root() {
  return (
    <Auth0ProviderWithRedirectCallback
      domain={import.meta.env.VITE_AUTH0_DOMAIN}
      clientId={import.meta.env.VITE_AUTH0_CLIENT_ID}
      authorizationParams={{
        redirect_uri: window.location.origin,
        audience: 'https://deltiologue.nerdwick.net/api'
      }}
    >
      <PrimeReactProvider>
        <div className="container mx-auto px-20">
          <Header />
          <Outlet />
          <Footer />
        </div>
      </PrimeReactProvider>
    </Auth0ProviderWithRedirectCallback>
  );
}
