import className from "classnames";
import { FunctionComponent } from "react";

export type TagSpec = {
  "tag_id": number;
  "tag_text": string;
  "tag_name": string;
  "category_id": number;
  "category_text": string;
  "category_name": string;
  "colour": string;
}

type TagProps = React.HTMLAttributes<HTMLDivElement> & { tag: TagSpec };

const Tag: FunctionComponent<TagProps> = ({ tag, children, ...rest }) => {

  const classes = className(
    `border py-0.5 px-1 text-xs rounded`,
    rest.className,
  );

  return (
    <span {...rest} className={classes} style={{ backgroundColor: `#${tag.colour}` }}>
      {tag.category_text}
      {children}
    </span>
  );
}

export default Tag;