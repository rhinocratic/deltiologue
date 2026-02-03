import { withAuthenticationRequired } from "@auth0/auth0-react";
import { ComponentType } from "react";

const Protected = ({ component, ...args }: { component: ComponentType<object> }) => {
  const Component = withAuthenticationRequired(component, args);
  return <Component />;
};

export default Protected;