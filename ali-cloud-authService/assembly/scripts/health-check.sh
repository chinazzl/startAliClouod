#!/bin/bash

# 应用健康检查脚本
# Usage: ./health-check.sh

APP_NAME="ali-cloud-web"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_DIR="$(dirname "$SCRIPT_DIR")"
PID_FILE="$APP_DIR/app.pid"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

log_warn() {
    echo -e "${YELLOW}[WARN]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查进程是否存在
check_process() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if kill -0 $PID 2>/dev/null; then
            log_info "Process is running with PID: $PID"
            return 0
        else
            log_warn "PID file exists but process is not running"
            rm -f "$PID_FILE"
            return 1
        fi
    else
        log_warn "PID file not found"
        return 1
    fi
}

# 检查端口是否监听
check_port() {
    local port=${1:-8010}
    if netstat -tlnp 2>/dev/null | grep -q ":$port "; then
        log_info "Port $port is listening"
        return 0
    else
        log_warn "Port $port is not listening"
        return 1
    fi
}

# 检查HTTP端点
check_http() {
    local url=${1:-http://localhost:8010}
    local timeout=${2:-5}

    if command -v curl &> /dev/null; then
        response=$(curl -s -o /dev/null -w "%{http_code}" --connect-timeout $timeout "$url" 2>/dev/null)
        if [ "$response" = "200" ]; then
            log_info "HTTP endpoint is healthy (200 OK)"
            return 0
        else
            log_warn "HTTP endpoint returned status: $response"
            return 1
        fi
    else
        log_warn "curl not available, skipping HTTP check"
        return 2
    fi
}

# 检查JVM状态
check_jvm() {
    if [ -f "$PID_FILE" ]; then
        PID=$(cat "$PID_FILE")
        if command -v jstat &> /dev/null; then
            echo "JVM Heap Usage:"
            jstat -gc $PID | tail -1 | awk '{printf "  Old Generation: %.2f%% used\n", ($4+$6+$8)/($3+$5+$7)*100}'

            echo "JVM CPU Time:"
            if command -v jstat &> /dev/null; then
                jstat -gcutil $PID 2>/dev/null | tail -1 | awk '{printf "  GC Time: %.2f%%\n", $NF}'
            fi
        fi
    fi
}

# 主检查逻辑
main() {
    log_info "=== Health Check for $APP_NAME ==="

    local status=0

    # 检查进程
    if ! check_process; then
        status=1
    fi

    # 检查端口
    if ! check_port 8010; then
        status=1
    fi

    # 检查HTTP端点
    if ! check_http "http://localhost:8010"; then
        status=1
    fi

    # JVM状态检查
    check_jvm

    echo ""
    if [ $status -eq 0 ]; then
        log_info "Overall Health: HEALTHY"
        exit 0
    else
        log_error "Overall Health: UNHEALTHY"
        exit 1
    fi
}

main "$@"