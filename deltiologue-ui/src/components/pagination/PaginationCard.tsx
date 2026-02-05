import className from "classnames";
import { FunctionComponent } from "react"
import { Exclusive } from "../util/exclusive";

type PaginationCardProps = React.HTMLAttributes<HTMLAnchorElement> &
  Partial<{
    selected?: boolean;
    ellipsis?: boolean;
    prev?: boolean;
    next?: boolean;
    regular?: boolean;
  }> &
  Exclusive<["regular", "selected", "ellipsis", "prev", "next"], boolean>

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

  const content = prev ? <>
    <span className="sr-only">Previous</span>
    <svg viewBox="0 0 20 20" fill="currentColor" data-slot="icon" aria-hidden="true" className="size-5">
      <path d="M11.78 5.22a.75.75 0 0 1 0 1.06L8.06 10l3.72 3.72a.75.75 0 1 1-1.06 1.06l-4.25-4.25a.75.75 0 0 1 0-1.06l4.25-4.25a.75.75 0 0 1 1.06 0Z" clip-rule="evenodd" fill-rule="evenodd" />
    </svg>
  </>
    : next ? <>
      <span className="sr-only">Next</span>
      <svg viewBox="0 0 20 20" fill="currentColor" data-slot="icon" aria-hidden="true" className="size-5">
        <path d="M8.22 5.22a.75.75 0 0 1 1.06 0l4.25 4.25a.75.75 0 0 1 0 1.06l-4.25 4.25a.75.75 0 0 1-1.06-1.06L11.94 10 8.22 6.28a.75.75 0 0 1 0-1.06Z" clip-rule="evenodd" fill-rule="evenodd" />
      </svg>
    </>
      : ellipsis ? "…"
        : children;

  return (
    <a href="#" {... (selected ? { "aria-current": "page" } : {})} {...rest} className={classes}>
      {content}
    </a>
  )
}

export default PaginationCard;