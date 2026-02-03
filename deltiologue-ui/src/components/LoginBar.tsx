import { useAuth0 } from "@auth0/auth0-react";

const LoginBar = () => {
  const { user, isAuthenticated, isLoading, loginWithRedirect, logout } = useAuth0();

  if (isLoading) {
    return <div className="absolute top-1 flex justify-between items-center flex-shrink-0 text-stone-600 text-xs">Loading profile...</div>;
  }

  return (
    isAuthenticated && user ? (
      <div className="absolute top-1 flex justify-between items-center flex-shrink-0 text-stone-600 text-xs">
        {user.name}
        <button className="text-blue-700 text-xs ml-1 underline"
          onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}
        >
          Log Out
        </button>
      </div>
    ) :
      <div className="absolute top-1 flex justify-between items-center flex-shrink-0 text-stone-600 text-xs">
        <button className="text-blue-700 text-xs ml-1 underline"
          onClick={() => loginWithRedirect()}
        >
          Log In
        </button>
      </div>
  );

}

export default LoginBar;