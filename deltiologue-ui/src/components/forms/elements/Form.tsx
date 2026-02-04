import className from "classnames";
import { FunctionComponent } from "react";

const Form: FunctionComponent<React.FormHTMLAttributes<HTMLFormElement>> = ({ children, ...rest }) => {

  const classes = className(
    "space-y-12",
    rest.className,
  );

  return (
    <form {...rest} className={classes}>
      {children}
    </form>
  );
}

export default Form;