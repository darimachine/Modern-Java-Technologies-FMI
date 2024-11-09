import bg.sofia.uni.fmi.mjt.socialnetwork.post.ReactionType;
import bg.sofia.uni.fmi.mjt.socialnetwork.post.SocialFeedPost;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.DefaultUserProfile;
import bg.sofia.uni.fmi.mjt.socialnetwork.profile.UserProfile;

import java.util.Map;
import java.util.Set;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.println("Hello and welcome!");
        UserProfile user = new DefaultUserProfile("Serhan");
        UserProfile user1 = new DefaultUserProfile("Random");
        SocialFeedPost post = new SocialFeedPost(user, "my content");
        post.addReaction(user, ReactionType.ANGRY);
        post.addReaction(user1, ReactionType.LOVE);
        post.removeReaction(user1);
        for (Map.Entry<ReactionType, Set<UserProfile>> react : post.getAllReactions().entrySet()) {
            System.out.println(react.getKey());
            System.out.println(react.getValue());
        }
    }
}