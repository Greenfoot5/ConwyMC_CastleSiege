package me.huntifi.castlesiege.voting;

import java.util.UUID;

public class VotesChanging {
	
	public static void setVotes(UUID uuid, String vote) {

		if (VotesLoading.PlayerVotes.containsKey(uuid)) {

			VotesLoading.PlayerVotes.remove(uuid);

		}

		VotesLoading.PlayerVotes.put(uuid, vote);

	}

	public static String getVotes(UUID uuid) {

		if (VotesLoading.PlayerVotes.containsKey(uuid)) {

			return VotesLoading.PlayerVotes.get(uuid);

		} else {

			return "NULL";
		}

	}
	
	public static void removeVote(UUID uuid, String removedvote) {
		
		String str = getVotes(uuid);

		if (VotesLoading.PlayerVotes.containsKey(uuid)) {
			
			VotesLoading.PlayerVotes.remove(uuid);

			VotesLoading.PlayerVotes.put(uuid, str.replace(removedvote, " "));

		} 

	}
	
	public static void addVote(UUID uuid, String addvote) {
		
		String str = getVotes(uuid);

		if (VotesLoading.PlayerVotes.containsKey(uuid)) {
			
			VotesLoading.PlayerVotes.remove(uuid);

			VotesLoading.PlayerVotes.put(uuid, str + addvote);

		} 

	}

}
