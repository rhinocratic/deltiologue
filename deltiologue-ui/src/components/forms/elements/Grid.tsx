import className from "classnames";
import { FunctionComponent } from "react";

const Grid: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    "mt-10 grid grid-cols-1 gap-x-6 gap-y-8 sm:grid-cols-6",
    rest.className,
  );

  return (
    <div {...rest} className={classes}>
      {children}
    </div>
  );
}

export default Grid;