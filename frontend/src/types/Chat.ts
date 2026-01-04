import type {User} from './User';

export interface Chat {
  id: number;
  name: string | null;
  ownerId: number;
  ownerUsername: string;
  participants: User[];
  participantCount: number;
}

export function getChatDisplayName(chat: Chat): string {
  return chat.name || chat.participants.map(p => p.username).join(', ');
}