import className from "classnames";
import { FunctionComponent } from "react";

const Paragraph: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "text-gray-500"
  );


  return (
    <p {...rest} className={classes}>
      {children}
    </p>
  );
}

export default Paragraph;