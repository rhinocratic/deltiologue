import className from "classnames";
import { FunctionComponent } from "react";

const Foot: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "mt-6 flex items-center justify-end gap-x-6"
  );


  return (
    <div {...rest} className={classes}>
      {children}
    </div>
  );
}

export default Foot;