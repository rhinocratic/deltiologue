import className from "classnames";
import { FunctionComponent } from "react";

const RadioButton: FunctionComponent<React.InputHTMLAttributes<HTMLInputElement>> = ({ children, ...rest }) => {

  const classes = className(
    "flex items-center gap-x-3",
    rest.className,
  );

  return (
    <div className={classes}>
      <input {...rest} type="radio" className="relative size-4 appearance-none rounded-full border border-gray-300 bg-white before:absolute before:inset-1 before:rounded-full before:bg-white not-checked:before:hidden checked:border-indigo-600 checked:bg-indigo-600 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:before:bg-gray-400 forced-colors:appearance-auto forced-colors:before:hidden" />
      {children}
    </div>
  );
}

export default RadioButton;