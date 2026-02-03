import { useAuth0 } from "@auth0/auth0-react";


export default async function Protected({ children }) {

  const { isAuthenticated, getIdTokenClaims } = useAuth0();
  const claims = await getIdTokenClaims();

  const authorized =
    isAuthenticated
    && claims
    && claims["edit"];

  return (
    {}
  );
}