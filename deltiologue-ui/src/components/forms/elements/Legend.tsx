import className from "classnames";
import { FunctionComponent } from "react";

const Legend: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "text-sm/6 font-semibold text-gray-900"
  );


  return (
    <legend {...rest} className={classes}>
      {children}
    </legend>
  );
}

export default Legend;