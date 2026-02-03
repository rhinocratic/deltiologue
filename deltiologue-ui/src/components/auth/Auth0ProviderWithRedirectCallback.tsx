import React, { PropsWithChildren } from 'react';
import { Auth0Provider, AppState, Auth0ContextInterface, User, Auth0ProviderOptions } from '@auth0/auth0-react';
import { useNavigate } from 'react-router-dom';

const Auth0ProviderWithRedirectCallback = ({
  children,
  context,
  ...props
}: PropsWithChildren<Omit<Auth0ProviderOptions, 'context'>> & {
  context?: React.Context<Auth0ContextInterface<User>>
}) => {
  const navigate = useNavigate();

  const onRedirectCallback = (appState?: AppState, user?: User) => {
    navigate((appState?.returnTo) || window.location.pathname);
  };

  return (
    <Auth0Provider
      onRedirectCallback={onRedirectCallback}
      context={context}
      {...props}
    >
      {children}
    </Auth0Provider>
  );
};

export default Auth0ProviderWithRedirectCallback;