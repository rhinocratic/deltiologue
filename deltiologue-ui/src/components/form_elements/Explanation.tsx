import className from "classnames";
import { FunctionComponent } from "react";

const Explanation: FunctionComponent<React.LabelHTMLAttributes<HTMLLabelElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "text-sm/6"
  );

  return (
    <div className={classes}>
      {children}
    </div>
  );
}

export default Explanation;

