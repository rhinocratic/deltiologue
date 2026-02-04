import className from "classnames";
import { FunctionComponent } from "react";

const Title: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    "text-base/7 font-semibold text-gray-900",
    rest.className,
  );

  return (
    <h2 {...rest} className={classes}>
      {children}
    </h2>
  );
}

export default Title;