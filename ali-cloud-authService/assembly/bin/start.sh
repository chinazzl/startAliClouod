#!/bin/bash

# AliCloud Web 应用启动脚本
# Usage: ./start.sh [dev|prod|test] [start|stop|restart|status]

# 设置脚本目录
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="$(dirname "$SCRIPT_DIR")"
APP_NAME="ali-cloud-web"
JAR_FILE="$APP_DIR/lib/ali-cloud-web.jar"

# JVM参数
JVM_OPTS="-Xms512m -Xmx2048m"
JVM_OPTS="$JVM_OPTS -XX:+UseG1GC"
JVM_OPTS="$JVM_OPTS -XX:MaxGCPauseMillis=200"
JVM_OPTS="$JVM_OPTS -XX:+HeapDumpOnOutOfMemoryError"
JVM_OPTS="$JVM_OPTS -XX:HeapDumpPath=$APP_DIR/logs/heapdump.hprof"

# Spring Boot参数
SPRING_OPTS="--spring.config.location=$APP_DIR/config/"
SPRING_OPTS="$SPRING_OPTS --spring.profiles.active=${1:-dev}"
SPRING_OPTS="$SPRING_OPTS --logging.file.path=$APP_DIR/logs"

# Nacos和数据库连接环境变量
export NACOS_USERNAME="${NACOS_USERNAME:-nacos}"
export NACOS_PASSWORD="${NACOS_PASSWORD:-zzl19920901}"
export DB_USERNAME="${DB_USERNAME:-zzl}"
export DB_PASSWORD="${DB_PASSWORD:-1qaz!QAZ}"

# PID文件
PID_FILE="$APP_DIR/app.pid"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 日志函数
log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Java环境
check_java() {
    if ! command -v java &> /dev/null; then
        log_error "Java not found in PATH"
        exit 1
    fi

    JAVA_VERSION=$(java -version 2>&1 | head -n1 | awk -F '"' '{print $2}')
    log_info "Java version: $JAVA_VERSION"
}

# 创建必要目录
create_dirs() {
    mkdir -p "$APP_DIR/logs"
    mkdir -p "$APP_DIR/temp"
}

# 启动应用
start_app() {
    if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
        log_warn "Application is already running with PID $(cat $PID_FILE)"
        return 1
    fi

    log_info "Starting $APP_NAME..."
    log_info "Environment: ${1:-dev}"
    log_info "Working directory: $APP_DIR"
    log_info "JAR file: $JAR_FILE"

    if [ ! -f "$JAR_FILE" ]; then
        log_error "JAR file not found: $JAR_FILE"
        exit 1
    fi

    # 启动应用
    cd "$APP_DIR"
    nohup java $JVM_OPTS -jar "$JAR_FILE" $SPRING_OPTS > "$APP_DIR/logs/startup.log" 2>&1 &

    PID=$!
    echo $PID > "$PID_FILE"

    # 等待启动
    sleep 3

    if kill -0 $PID 2>/dev/null; then
        log_info "Application started successfully with PID: $PID"
        log_info "Log file: $APP_DIR/logs/startup.log"
        log_info "PID file: $PID_FILE"
    else
        log_error "Application failed to start"
        rm -f "$PID_FILE"
        exit 1
    fi
}

# 停止应用
stop_app() {
    if [ ! -f "$PID_FILE" ]; then
        log_warn "PID file not found. Application may not be running."
        return 1
    fi

    PID=$(cat "$PID_FILE")
    log_info "Stopping application with PID: $PID"

    if kill -0 $PID 2>/dev/null; then
        kill $PID

        # 等待进程结束
        for i in {1..30}; do
            if ! kill -0 $PID 2>/dev/null; then
                break
            fi
            sleep 1
        done

        # 如果进程还在，强制杀死
        if kill -0 $PID 2>/dev/null; then
            log_warn "Application didn't stop gracefully, forcing kill..."
            kill -9 $PID
        fi

        rm -f "$PID_FILE"
        log_info "Application stopped successfully"
    else
        log_warn "Process with PID $PID is not running"
        rm -f "$PID_FILE"
    fi
}

# 重启应用
restart_app() {
    stop_app
    sleep 2
    start_app $1
}

# 查看状态
status_app() {
    if [ -f "$PID_FILE" ] && kill -0 $(cat "$PID_FILE") 2>/dev/null; then
        PID=$(cat "$PID_FILE")
        log_info "Application is running with PID: $PID"

        # 显示进程信息
        ps -p $PID -o pid,ppid,etime,cmd --no-headers
    else
        log_warn "Application is not running"
    fi
}

# 显示帮助信息
show_help() {
    echo "Usage: $0 [environment] [command]"
    echo ""
    echo "Environment:"
    echo "  dev     Development environment (default)"
    echo "  prod    Production environment"
    echo "  test    Test environment"
    echo ""
    echo "Commands:"
    echo "  start   Start the application"
    echo "  stop    Stop the application"
    echo "  restart Restart the application"
    echo "  status  Show application status"
    echo ""
    echo "Examples:"
    echo "  $0 dev start     # Start in development mode"
    echo "  $0 prod restart  # Restart in production mode"
    echo "  $0 status        # Show status"
}

# 主逻辑
main() {
    check_java
    create_dirs

    ENV=${1:-dev}
    COMMAND=${2:-start}

    case $COMMAND in
        start)
            start_app $ENV
            ;;
        stop)
            stop_app
            ;;
        restart)
            restart_app $ENV
            ;;
        status)
            status_app
            ;;
        help|--help|-h)
            show_help
            ;;
        *)
            log_error "Unknown command: $COMMAND"
            show_help
            exit 1
            ;;
    esac
}

# 执行主函数
main "$@"