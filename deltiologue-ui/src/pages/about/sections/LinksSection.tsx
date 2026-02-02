
interface ContentSectionProps {
  title: string;
  content: string;
}

export default function LinksSection({ title, content }: ContentSectionProps) {
  return (
    <div>
      <div>Links: {title}</div>
      <div>{content}</div>
    </div>
  );
}