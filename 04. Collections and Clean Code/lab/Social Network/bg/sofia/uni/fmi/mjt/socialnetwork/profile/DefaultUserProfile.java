package bg.sofia.uni.fmi.mjt.socialnetwork.profile;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import java.util.Objects;
import java.util.Set;

public class DefaultUserProfile implements UserProfile {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultUserProfile that = (DefaultUserProfile) o;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(username);
    }

    private final String username;
    Set<Interest> interests;
    Set<UserProfile> friends;

    private <T> void checkForNull(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Parameter is null");
        }
    }

    public DefaultUserProfile(String username) {
        this.username = username;
        this.interests = new HashSet<Interest>();
        this.friends = new HashSet<UserProfile>();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public Collection<Interest> getInterests() {
        return Collections.unmodifiableSet(interests);
    }

    @Override
    public boolean addInterest(Interest interest) {

        checkForNull(interest);
        return interests.add(interest);
    }

    @Override
    public boolean removeInterest(Interest interest) {
        checkForNull(interest);
        return interests.remove(interest);
    }

    @Override
    public Collection<UserProfile> getFriends() {
        return Collections.unmodifiableSet(friends);
    }

    @Override
    public boolean addFriend(UserProfile userProfile) {
        if (userProfile == null || userProfile == this) {
            throw new IllegalArgumentException("UserProfile Null");
        }
        boolean isAdded = friends.add(userProfile);
        if (isAdded) {
            userProfile.addFriend(this);
        }
        return isAdded;
    }

    @Override
    public boolean unfriend(UserProfile userProfile) {
        checkForNull(userProfile);
        boolean isRemoved = friends.remove(userProfile);
        if (isRemoved) {
            userProfile.unfriend(this);
        }
        return isRemoved;
    }

    @Override
    public boolean isFriend(UserProfile userProfile) {
        checkForNull(userProfile);
        return friends.contains(userProfile);
    }

    @Override
    public int compareTo(UserProfile o) {
        int currentFriends = this.friends.size();
        int otherFriends = o.getFriends().size();
        return Integer.compare(otherFriends, currentFriends);
    }
}
