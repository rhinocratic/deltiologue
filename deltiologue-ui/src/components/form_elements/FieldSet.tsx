import className from "classnames";
import { FunctionComponent } from "react";

const FieldSet: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "mt-10 space-y-10"
  );


  return (
    <div {...rest} className={classes}>
      <fieldset>
        {children}
      </fieldset>
    </div>
  );
}

export default FieldSet;