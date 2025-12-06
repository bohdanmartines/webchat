
export interface Message {
  id: number;
  chatId: number;
  userId: number;
  username: string;
  content: string;
  timestamp?: string;
}