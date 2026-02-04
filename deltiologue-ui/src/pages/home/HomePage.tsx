import { useAuth0 } from "@auth0/auth0-react";
import ApiCall from "../../components/ApiCall";
import { useLocation } from "react-router-dom";
import ExampleForm from "../../components/forms/ExampleForm";

export default function HomePage() {

  const { isAuthenticated, user, error } = useAuth0<{
    name: string;
  }>();
  const { pathname } = useLocation();

  console.log("Authenticated: " + isAuthenticated);
  console.log(error);

  return (
    <div>
      <div>Home Page</div>
      <div>Authenticated: {isAuthenticated ? "true" : "false"}</div>
      <div>User: {user?.name}</div>
      <div>Pathname: {pathname}</div>
      <ExampleForm />
      <ApiCall />
    </div>
  );
}