import className from "classnames";
import { FunctionComponent } from "react";

const TextField: FunctionComponent<React.InputHTMLAttributes<HTMLInputElement>> = ({ prefix, ...rest }) => {

  const classes = className(
    "mt-2",
    rest.className,
  );

  return (
    <div className="mt-2">
      {prefix ?
        <div className={classes}>
          <div className="flex items-center rounded-md bg-white pl-3 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600">
            <div className="shrink-0 text-base text-gray-500 select-none sm:text-sm/6">{prefix}</div>
            <input id="username" type="text" name="username" placeholder="janesmith" className="block min-w-0 grow bg-white py-1.5 pr-3 pl-1 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6" />
          </div>
        </div>
        :
        <div className={classes}>
          <input {...rest} className="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6" />
        </div>
      }
    </div>
  );
}

export default TextField;
