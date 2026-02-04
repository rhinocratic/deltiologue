import className from "classnames";
import { FunctionComponent } from "react";

const CheckBoxField: FunctionComponent<React.HTMLAttributes<HTMLDivElement>> = ({ children, ...rest }) => {

  const classes = className(
    rest.className,
    "mt-6 space-y-6"
  );

  return (
    <div className={classes}>
      <div className="flex gap-3">
        {children}
      </div>
    </div>
  );
}

export default CheckBoxField;