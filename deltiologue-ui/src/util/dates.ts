
export const toShortDate = (isoDate: string): string => {
  const timestamp = Date.parse(isoDate);
  return new Date(timestamp).toLocaleDateString();
}