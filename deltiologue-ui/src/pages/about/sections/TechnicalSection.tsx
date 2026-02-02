
interface ContentSectionProps {
  title: string;
  content: string;
}

export default function TechnicalSection({ title, content }: ContentSectionProps) {
  return (
    <div>
      <div>Technical: {title}</div>
      <div>{content}</div>
    </div>
  );
}