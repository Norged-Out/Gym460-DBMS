# Checkout main and pull the latest changes
git checkout main
git pull origin main

# Iterate through all local branches (except main)
for branch in $(git branch --list | grep -v "main"); do
    # Checkout the branch
    git checkout $branch
    
    # Merge with main
    git merge main
    
    # Push the changes to the remote branch
    git push origin $branch
done

# Return to the main branch
git checkout main
