# Variables
SB_HOST = your-ec2-instance-ip
USER = ubuntu
CURRENT_BRANCH = $(shell git rev-parse --abbrev-ref HEAD)
CURRENT_MESSAGE = $(shell git log -1 --pretty=%B)
CURRENT_DATE = $(shell date +"%Y-%m-%d %T")

# Syncing and setting usage
set-usage:
	ssh ${SB_HOST} "echo -e \"${SB_HOST} \t ${USER} \t ${CURRENT_BRANCH} \t ${CURRENT_MESSAGE} \t ${CURRENT_DATE}\" > \"/home/ubuntu/currentuser.txt\""

sync: set-usage
	rsync --exclude ".git" --exclude ".idea" --exclude ".bin" --exclude ".bak" --exclude "build" --exclude "*.log" --exclude "out" --exclude "tmp" \
     -avp --delete --stats . ${USER}@${SB_HOST}:~/Split-Expense

# Start, stop and migrate tasks
stop:
	ssh ${SB_HOST} pkill -f java || true

start: stop
	ssh ${SB_HOST} "cd accelerate; nohup ./gradlew dev run --parallel > dev.log 2>&1 &"

tail:
	ssh ${SB_HOST} tail -F Split-Expense/dev.log

deploy: sync start tail

.PHONY: set-usage sync stop start tail deploy