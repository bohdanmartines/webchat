import type {User} from './User';

export interface Chat {
  id: number;
  name: string | null;
  participants: User[];
}

export function getChatDisplayName(chat: Chat): string {
  return chat.name || chat.participants.map(p => p.username).join(', ');
}