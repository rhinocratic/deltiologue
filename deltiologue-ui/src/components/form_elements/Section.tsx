import className from "classnames";
import { FunctionComponent } from "react";

const Section: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    "border-b border-gray-900/10 pb-12",
    rest.className,
  );

  return (
    <div {...rest} className={classes}>
      {children}
    </div>
  );
}

export default Section;