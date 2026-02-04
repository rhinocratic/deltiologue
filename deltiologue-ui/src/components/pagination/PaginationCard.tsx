import className from "classnames";
import { FunctionComponent } from "react"
import { Exclusive } from "../util/exclusive";

type PaginationCardProps = React.HTMLAttributes<HTMLAnchorElement> &
  Partial<{
    selected?: boolean;
    ellipsis?: boolean;
    prev?: boolean;
    next?: boolean;
  }> &
  Exclusive<["selected", "ellipsis", "prev", "next"], boolean>;

const PaginationCard: FunctionComponent<PaginationCardProps> = ({ selected, ellipsis, prev, next, children, ...rest }) => {

  const classes = className(
    selected ?
      "relative z-10 inline-flex items-center bg-indigo-600 px-4 py-2 text-sm font-semibold text-white focus:z-20 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      : ellipsis ?
        "relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-700 inset-ring inset-ring-gray-300 focus:outline-offset-0"
        : (prev || next) ?
          "relative inline-flex items-center rounded-l-md px-2 py-2 text-gray-400 inset-ring inset-ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0"
          : "relative inline-flex items-center px-4 py-2 text-sm font-semibold text-gray-900 inset-ring inset-ring-gray-300 hover:bg-gray-50 focus:z-20 focus:outline-offset-0",
    rest.className,
  );

  return (
    <a href="#" aria-current="page" {...rest} className={classes}>
      {children}
    </a>
  )
}

export default PaginationCard;