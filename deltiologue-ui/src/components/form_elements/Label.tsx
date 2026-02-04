import className from "classnames";
import { FunctionComponent } from "react";

const Label: FunctionComponent<React.LabelHTMLAttributes<HTMLLabelElement>> = ({ children, ...rest }) => {

  const classes = className(
    "block text-sm/6 font-medium text-gray-900",
    rest.className,
  );

  return (
    <label {...rest} className={classes}>
      {children}
    </label>
  );
}

export default Label;