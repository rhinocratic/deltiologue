import className from "classnames";
import { FunctionComponent } from "react";
import { Exclusive } from "../../util/exclusive";

type ButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> &
  Partial<{
    primary?: boolean;
    secondary?: boolean;
  }> &
  Exclusive<["primary", "secondary"], boolean>;


const Button: FunctionComponent<ButtonProps> = ({ primary, secondary, children, ...rest }) => {

  const classes = className(
    rest.className,
    {
      "rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600": primary,
      "text-sm/6 font-semibold text-gray-900": secondary
    }
  );

  return (
    <button {...rest} className={classes}>
      {children}
    </button>
  );
}

export default Button;