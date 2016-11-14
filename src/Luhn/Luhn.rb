answer = ""
map = {1 => 5, 3 => 6, 5 => 7, 7 => 8, 9 => 9}
f = File.open(("#{Dir.pwd}/input.txt"))
f.each_line do |line|
  line.reverse!
  line = line.strip
  sum = 0
  x_i = 0
  line.split("").each_with_index do |d, index|
    if d == 'X'
      x_i = index
    else
      digit = d.to_i * ((index % 2) + 1)
        if digit < 10
          sum += digit
        else
          digit.to_s.chars.map(&:to_i).each do |i|
            sum += i
          end
        end
    end
  end
  if x_i % 2 == 0
    x = (10 - (sum % 10)) % 10
  else
    x = (10 - (sum % 10)) % 10
    if x % 2 == 0
      x = x / 2
    else
      x = map[x]
    end
  end
  answer = answer + x.to_s
end
f.close

puts answer