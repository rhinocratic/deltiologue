import className from "classnames";
import { FunctionComponent } from "react";

const TextField: FunctionComponent<React.InputHTMLAttributes<HTMLInputElement>> = ({ ...rest }) => {

  const classes = className(
    "block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6",
    rest.className,
  );

  return (
    <div className="mt-2">
      <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
        <input {...rest} className={classes} />
      </div>
    </div>
  );
}

export default TextField;