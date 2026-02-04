import className from "classnames";
import { FunctionComponent } from "react";

const RadioButtonGroup: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "mt-6 space-y-6"
  );

  return (
    <div className={classes}>
      {children}
    </div>
  );
}

export default RadioButtonGroup;